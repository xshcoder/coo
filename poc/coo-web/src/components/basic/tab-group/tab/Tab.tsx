import { FC, MouseEventHandler } from 'react';
import styles from './Tab.module.scss';

interface TabProps {
  label: string;
  isSelected: boolean;
  onClick: MouseEventHandler<HTMLButtonElement>;
}

const Tab: FC<TabProps> = ({ label, isSelected, onClick }) => {
  const tabClasses = [
    styles.tab,
    isSelected ? styles['tab-selected'] : '',
  ].filter(Boolean).join(' ');

  return (
    <button className={tabClasses} onClick={onClick} role="tab" aria-selected={isSelected}>
      {label}
    </button>
  );
};

export default Tab;