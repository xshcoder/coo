import { useState, useEffect } from 'react';
import {
    PersonalizeControllerApi,
    GetTimelineRequest,
    TimelineCursor,
} from '@/api/generated/personalize';
import {
    UserControllerApi,
    GetUsersByIdsRequest,
    User,
} from '@/api/generated/users'; // Assuming this path exists
import {
    StatisticsControllerApi,
    GetCoosStatisticsRequest,
    Statistics,
} from '@/api/generated/statistics';
import { TimelineModel } from '@/types/TimelineModel';

const personalizeApi = new PersonalizeControllerApi();
const userApi = new UserControllerApi();
const statisticsApi = new StatisticsControllerApi();

export const useTimeline = (userId: string | undefined) => {
    const [timelineData, setTimelineData] = useState<TimelineModel[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<any>(null);
    const [cursor, setCursor] = useState<string | undefined>(undefined);
    const [hasNextPage, setHasNextPage] = useState<boolean>(true);

    const fetchTimeline = async (currentCursor?: string) => {
        if (!userId || !hasNextPage) return;

        setLoading(true);
        setError(null);

        try {
            const timelineRequest: GetTimelineRequest = {
                userId,
                cursor: currentCursor,
                // limit: 10 // Optional: Add limit if needed
            };
            const timelineResult: TimelineCursor = await personalizeApi.getTimeline(timelineRequest);

            if (timelineResult.activities && timelineResult.activities.length > 0) {
                const activities = timelineResult.activities;

                // 1. Extract unique User IDs and Coo IDs
                const userIds = new Set<string>();
                const cooIds = new Set<string>();

                activities.forEach(activity => {
                    if (activity.userId) userIds.add(activity.userId);
                    // Add other relevant user IDs if necessary (e.g., from interactions)
                    if (activity.cooId) cooIds.add(activity.cooId);
                });

                const uniqueUserIds = Array.from(userIds);
                const uniqueCooIds = Array.from(cooIds);

                // 2. Fetch Users and Statistics in parallel
                let usersMap: Map<string, User> = new Map();
                let statsMap: Map<string, Statistics> = new Map();

                const fetchPromises = [];

                if (uniqueUserIds.length > 0) {
                    const usersRequest: GetUsersByIdsRequest = { requestBody: uniqueUserIds };
                    fetchPromises.push(
                        userApi.getUsersByIds(usersRequest).then(users => { // Assuming getUsersByIds exists
                            users.forEach(user => {
                                if (user.id) usersMap.set(user.id, user);
                            });
                        }).catch(err => {
                            console.error("Failed to fetch users:", err);
                            // Decide how to handle partial failures
                        })
                    );
                }

                if (uniqueCooIds.length > 0) {
                    const statsRequest: GetCoosStatisticsRequest = { requestBody: uniqueCooIds };
                    fetchPromises.push(
                        statisticsApi.getCoosStatistics(statsRequest).then(statsList => {
                            statsList.forEach(stats => {
                                if (stats.subjectId) {
                                     statsMap.set(stats.subjectId, stats);
                                }
                            });
                        }).catch(err => {
                            console.error("Failed to fetch statistics:", err);
                            // Decide how to handle partial failures
                        })
                    );
                }

                await Promise.all(fetchPromises);

                // 3. Assemble TimelineModel
                const assembledData: TimelineModel[] = activities.map(activity => {
                    const user = activity.userId ? usersMap.get(activity.userId) : undefined;
                    // Use subjectId from statistics to match cooId from activity
                    const statistics = activity.cooId ? statsMap.get(activity.cooId) : undefined;

                    // Create the TimelineModel object by spreading activity and adding mapped fields
                    return {
                        ...activity, // Spread properties from TimelineActivity
                        // Map user data (provide defaults if user is not found)
                        username: user?.name ?? 'Unknown User', // Use 'name' from User model
                        userhandle: user?.handle ?? 'unknownhandle', // Use 'handle' from User model
                        userlogo: user?.logo ?? '/src/assets/images/avatar1.svg', // Use 'profileImageUrl' or default
                        // Map statistics data (provide defaults if stats are not found)
                        repliescount: statistics?.repliesCount ?? 0, // Use 'repliesCount' from Statistics model
                        likescount: statistics?.likesCount ?? 0, // Use 'likesCount' from Statistics model
                        viewscount: statistics?.viewsCount ?? 0, // Use 'viewsCount' from Statistics model
                    };
                });

                // Append new data if cursor exists (pagination), otherwise set new data
                setTimelineData(prevData => currentCursor ? [...prevData, ...assembledData] : assembledData);
                setCursor(timelineResult.nextCursor);
                setHasNextPage(timelineResult.hasMore??false);

            } else {
                 // No more activities or initial fetch empty
                 if (!currentCursor) {
                    setTimelineData([]); // Reset if initial fetch is empty
                 }
                 setHasNextPage(false);
            }

        } catch (err) {
            console.error("Failed to fetch timeline:", err);
            setError(err);
            // Optionally reset data on error
            // setTimelineData([]);
            // setHasNextPage(true);
            // setCursor(undefined);
        } finally {
            setLoading(false);
        }
    };

    // Initial fetch when userId changes
    useEffect(() => {
        setTimelineData([]); // Reset data when userId changes
        setCursor(undefined);
        setHasNextPage(true);
        if (userId) {
            fetchTimeline(undefined); // Pass undefined cursor for initial fetch
        }
    }, [userId]);

    // Function to fetch the next page
    const loadMore = () => {
        if (!loading && hasNextPage) {
            fetchTimeline(cursor);
        }
    };

    return { timelineData, loading, error, loadMore, hasNextPage };
};