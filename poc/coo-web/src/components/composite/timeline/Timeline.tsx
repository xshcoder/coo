import { FC } from 'react';
// Update the import path and component name
import TimelineActivity from '../timelineactivity/TimelineActivity';
import styles from './Timeline.module.scss'; // Optional: Add styles if needed

// Dummy data for activitys (can keep variable name or rename to dummyActivities)
const dummyTimelineActivities = Array.from({ length: 10 }, (_, i) => ({
  id: `activity-${i + 1}`, // Add a unique key
  userName: `User ${i + 1}`,
  userHandle: `user${i + 1}handle`,
  createdAt: `${i + 1}h ago`, // Simple relative time
  content: `This is the content for dummy activity number ${i + 1}. Lorem ipsum dolor sit amet, consectetur adipiscing elit.`,
  replyCount: Math.floor(Math.random() * 50),
  likeCount: Math.floor(Math.random() * 500),
  viewCount: Math.floor(Math.random() * 10000),
  userIconUrl: '/src/assets/images/avatar1.svg', // Corrected path
}));

const Timeline: FC = () => {
  return (
    <div className={styles.timeline}> {/* Optional wrapper */}
      {dummyTimelineActivities.map((activity) => (
        // Update the component usage
        <TimelineActivity
          key={activity.id} // Use unique key for list rendering
          userName={activity.userName}
          userHandle={activity.userHandle}
          createdAt={activity.createdAt}
          content={activity.content}
          replyCount={activity.replyCount}
          likeCount={activity.likeCount}
          viewCount={activity.viewCount}
          userIconUrl={activity.userIconUrl}
        />
      ))}
    </div>
  );
};

export default Timeline;