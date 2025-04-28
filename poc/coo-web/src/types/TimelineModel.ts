import { TimelineActivity } from '../api/generated/personalize/models/TimelineActivity'; // Adjust path if needed

/**
 * Represents a timeline item for display purposes, extending the base API model
 * with additional user and statistics details potentially aggregated from other sources.
 */
export interface TimelineModel extends TimelineActivity {
  username: string;
  userhandle: string;
  userlogo: string; // Assuming this is a URL or path string
  repliescount: number;
  likescount: number;
  viewscount: number;
}