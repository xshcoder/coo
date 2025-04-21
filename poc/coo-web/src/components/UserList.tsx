import { FC, useEffect, useState } from 'react';
import { User } from '../api/generated/users';
import { userApi } from '../api/client/userApi';

const UserList: FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await userApi.getUsers({page: 0, size: 10});
        setUsers(response.content ?? []);
      } catch (error) {
        console.error('Failed to fetch users:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  if (loading) {
    return <div>Loading users...</div>;
  }

  return (
    <div>
      <h2>Users</h2>
      <ul>
        {users.map((user) => (
          <li key={user.id}>
            <h3>{user.name}</h3>
            <p>@{user.handle}</p>
            {user.bio && <p>{user.bio}</p>}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserList;