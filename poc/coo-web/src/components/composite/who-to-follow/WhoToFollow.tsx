import { FC } from 'react';
import styles from './WhoToFollow.module.scss';
import FollowSuggestion from '../follow-suggestion/FollowSuggestion';
import { User } from '../../../api/generated/users/models/User'; // Import the User model

// Generate dummy data conforming to the User model structure
const dummySuggestions: User[] = Array.from({ length: 5 }, (_, i) => ({
  id: `user-${i + 1}`, // Assuming User has an id field
  name: `Suggested User ${i + 1}`, // Assuming User has a name field
  handle: `suggested${i + 1}_handle`, // Assuming User has a handle field
  logo: '/src/assets/images/avatar1.svg', // Assuming User has profileImageUrl
  // Add other required User fields here with default/dummy values if necessary
  // email: `user${i+1}@example.com`,
  // bio: `This is the bio for user ${i+1}`,
  // etc...
}));


const WhoToFollow: FC = () => {
  return (
    <div className={styles.whoToFollow}>
      <h3>Who to follow</h3>
      <div className={styles.suggestionList}>
        {dummySuggestions.map((user) => (
          // Pass the required props from the user object
          <FollowSuggestion
            key={user.id}
            id={user.id}
            name={user.name}
            handle={user.handle}
            logo={user.logo}
            // Pass any other required props from the User model
          />
        ))}
        {/* Optional: Add a "Show more" link */}
        {/* <a href="#" className={styles.showMore}>Show more</a> */}
      </div>
    </div>
  );
};

export default WhoToFollow;