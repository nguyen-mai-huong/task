import { useDispatch, useSelector } from "react-redux";
import { Redirect } from "react-router-dom";

import { selectIsLogined, selectUsername } from "../userSelector";
import httpClient from '../../httpClient';
import { logOut } from "../userActions";
import { UserState } from "../UserState";

import { Container, Typography, Button } from '@material-ui/core';
import useStyles from '../../style/styles';

const Home = () => {
  const isLogined = useSelector(selectIsLogined);
  const username = useSelector(selectUsername);

  const dispatch = useDispatch();
  
  const classes = useStyles();

  if (!isLogined) {
    return <Redirect to="/" />;
  }

  const handleLogout = (): void => {
    httpClient.get("/user/logout").then((response) => {
      console.log("Response: ", response);

      const successUserLogout: UserState = {
        isLogined: false
      }
      dispatch(logOut(successUserLogout));
    }).catch((error) => {
      console.log("Error: ", error);
    })

  }


  return (
    <Container>
      <Typography component="h5">
        Welcome home, {username}
      </Typography>
      <Button
          variant="text"
          color="primary"
          type="submit"   
          onClick={handleLogout}
          className={classes.submit}          
      >
        Log out
      </Button>
    </Container>
  )
}

export default Home;