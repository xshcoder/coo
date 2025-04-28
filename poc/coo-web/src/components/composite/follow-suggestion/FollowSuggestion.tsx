import { FC } from 'react';
import styles from './FollowSuggestion.module.scss';
import Button from '../../basic/button/Button'; // Import the Button component
import { User } from '../../../api/generated/users/models/User'; // Import the User model

// Remove the old FollowSuggestionProps interface
// interface FollowSuggestionProps {
//   userIconUrl: string;
//   userName: string;
//   userHandle: string;
// }

// Use the User model for props
const FollowSuggestion: FC<User> = ({
  id, // Assuming User has an id, though not directly used here yet
  name,
  handle,
  logo,
}) => {
  // Default profile image if none provided
  const iconUrl = logo || '/src/assets/images/avatar1.svg';

  return (
    <div className={styles.followSuggestion}>
      <div className={styles.userInfo}>
        {/* Use profileImageUrl and name/handle from User model */}
        <img src={iconUrl} alt={`${name} icon`} className={styles.userIcon} />
        <div className={styles.names}>
          <span className={styles.userName}>{name}</span>
          <span className={styles.userHandle}>@{handle}</span>
        </div>
      </div>
      <Button className={styles.followButton}>Follow</Button>
    </div>
  );
};

export default FollowSuggestion;