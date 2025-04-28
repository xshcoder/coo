import { FC } from 'react';
// Update the SCSS module import
import styles from './TimelineActivity.module.scss';
// Import the SVG icons
import ReplyIcon from '../../../assets/images/replies.svg';
import LikeIcon from '../../../assets/images/likes.svg';
import ViewIcon from '../../../assets/images/views.svg';

// Rename the props interface
interface TimelineActivityProps {
  userName: string;
  userHandle: string;
  createdAt: string; // Or Date object, adjust as needed
  content: string;
  replyCount: number;
  likeCount: number;
  viewCount: number;
  userIconUrl?: string; // Optional URL for user icon
}

// Rename the component function
const TimelineActivity: FC<TimelineActivityProps> = ({
  userName,
  userHandle,
  createdAt,
  content,
  replyCount,
  likeCount,
  viewCount,
  userIconUrl = '/src/assets/images/avatar1.svg', // Default icon path
}) => {
  return (
    // Update the main container class name if you changed it in the SCSS file
    <div className={styles.timelineActivityContainer}>
      <div className={styles.userIcon}>
        <img src={userIconUrl} alt="User Icon" className={styles.userIcon} />
      </div>
      {/* Keep internal structure class names unless changed in SCSS */}
      <div className={styles.timelineActivity}>
        <div className={styles.header}>
          <div className={styles.userInfo}>
            <span className={styles.userName}>{userName}</span>
            <span className={styles.userHandle}>@{userHandle}</span>
            <span className={styles.separator}>·</span>
            <span className={styles.createdAt}>{createdAt}</span>
          </div>
        </div>
        <div className={styles.content}>
          {content}
        </div>
        <div className={styles.footer}>
          <div className={styles.action}>
            <img src={ReplyIcon} alt="Reply" className={styles.actionIcon} />
            <span>{replyCount}</span>
          </div>
          <div className={styles.action}>
            <img src={LikeIcon} alt="Like" className={styles.actionIcon} />
            <span>{likeCount}</span>
          </div>
          <div className={styles.action}>
            <img src={ViewIcon} alt="View" className={styles.actionIcon} />
            <span>{viewCount}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

// Update the export
export default TimelineActivity;