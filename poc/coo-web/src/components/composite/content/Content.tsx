import { FC } from 'react';
import styles from './Content.module.scss';
import TabGroup from '../../basic/tab-group/TabGroup'; // Import TabGroup
import Timeline from '../timeline/Timeline'; // Import placeholder
import SideContent from '../side-content/SideContent'; // Import placeholder
import Following from '../following/Following';

const Content: FC = () => {
  // Define the tabs data
  const tabData = [
    { label: 'Timeline', content: <Timeline /> },
    { label: 'Following', content: <Following /> },
  ];

  return (
    <div className={styles.content}>
      <div className={styles['main-content']}>
        <TabGroup tabs={tabData} initialTabIndex={0} />
      </div>
      <div className={styles['side-content']}>
        <SideContent />
      </div>
    </div>
  );
};

export default Content;