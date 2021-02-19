import { Typography } from '@material-ui/core';
import { useSelector } from 'react-redux';
import { selectUsername } from '../userSelector';

export const Home = () => {
  const username = useSelector(selectUsername);
  
  return (
    <div>
      <Typography component="span">
        Welcome home, {username}
      </Typography>
    </div>
  );
}