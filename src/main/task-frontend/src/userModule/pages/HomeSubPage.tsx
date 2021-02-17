import { Typography } from '@material-ui/core';

export const Home = (props: any) => {
  return (
    <div>
      <Typography component="span">
        Welcome home, {props.username}
      </Typography>
    </div>
  );
}