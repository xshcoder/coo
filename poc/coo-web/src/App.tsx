import { FC } from 'react';
import Login from './components/Login';
import UserList from './components/UserList';

import './styles/variables.scss';
import './styles/login.scss';

const App: FC = () => {
  return (
    <div>
      <Login />
      <UserList />
    </div>
  );
};

export default App;