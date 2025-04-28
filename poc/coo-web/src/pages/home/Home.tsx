import { FC } from 'react';
import TopNavigation from '../../components/composite/top-navigation/TopNavigation';
import Content from '../../components/composite/content/Content';
import React from 'react';

const Home: FC = () => {
  return (
    <React.Fragment>
      <TopNavigation/>
      <Content/>
    </React.Fragment>
  );
};

export default Home;