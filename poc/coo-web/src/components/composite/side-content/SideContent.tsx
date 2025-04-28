import { FC } from 'react';
import styles from './SideContent.module.scss'; // Ensure this path is correct
import WhoToFollow from '../who-to-follow/WhoToFollow'; // Import the new component

const SideContent: FC = () => {
  return (
    <div className={styles['side-content']}>
      {/* You can add other side content components here as well */}
      <WhoToFollow />
      {/* Example: Add more components if needed */}
      {/* <div>Other Side Content</div> */}
    </div>
  );
};

export default SideContent;