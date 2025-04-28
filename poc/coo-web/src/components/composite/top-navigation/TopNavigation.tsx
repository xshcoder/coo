import { FC } from 'react';
import styles from './TopNavigation.module.scss';
import Input from '../../basic/input/Input';
import Button from '../../basic/button/Button';
import logoSvg from '../../../assets/images/coo.svg';
import searchIcon from '../../../assets/images/search.svg'; // Import the search icon

const TopNavigation: FC = () => {
  return (
    <div className={styles['top-navigation']}>
      <div className={styles.logo}>
        <img src={logoSvg} alt="COO Logo" height="32" />
      </div>
      <div className={styles.search}>
        {/* Pass the imported icon to the Input component */}
        <Input
          placeholder="Search..."
          radius="m"
          iconLeft={searchIcon} // Use the new prop
        />
      </div>
      <div className={styles.login}>
        <Button primary>Login</Button>
      </div>
    </div>
  );
};

export default TopNavigation;