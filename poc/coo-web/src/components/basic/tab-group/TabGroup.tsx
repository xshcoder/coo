import { FC, useState, ReactNode } from 'react';
import Tab from './tab/Tab';
import styles from './TabGroup.module.scss';

interface TabInfo {
  label: string;
  content: ReactNode;
}

interface TabGroupProps {
  tabs: TabInfo[];
  initialTabIndex?: number;
}

const TabGroup: FC<TabGroupProps> = ({ tabs = [], initialTabIndex = 0 }) => {
  const [activeIndex, setActiveIndex] = useState<number>(() => {
    // Ensure initial index is valid
    return initialTabIndex >= 0 && initialTabIndex < tabs.length ? initialTabIndex : 0;
  });

  if (tabs.length === 0) {
    return null; // Don't render anything if there are no tabs
  }

  return (
    <div className={styles['tab-group']}>
      <div className={styles['tab-list']} role="tablist">
        {tabs.map((tab, index) => (
          <Tab
            key={tab.label} // Use label as key, ensure labels are unique
            label={tab.label}
            isSelected={index === activeIndex}
            onClick={() => setActiveIndex(index)}
          />
        ))}
      </div>
      <div className={styles['tab-content']} role="tabpanel">
        {/* Render the content of the active tab */}
        {tabs[activeIndex]?.content}
      </div>
    </div>
  );
};

export default TabGroup;