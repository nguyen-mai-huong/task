import { ChangeEvent, FormEvent, SyntheticEvent } from 'react';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import httpClient from '../../httpClient';

import { Grid, Typography, TextField, Button, CssBaseline, Paper, Box, Link } from '@material-ui/core';
import Copyright from '../../components/Copyright';
import useStyles from '../../style/styles';

import { UserState } from '../UserState';
import { logIn } from '../userActions';
import { selectIsLogined } from '../userSelector';
import { Redirect } from 'react-router-dom';

const Login = () => {
  const classes = useStyles();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const dispatch = useDispatch();

  const handleUsername = (event: ChangeEvent<HTMLInputElement>): void => {
    setUsername(event.target.value);
  }

  const handlePassword = (event: ChangeEvent<HTMLInputElement>): void => {
    setPassword(event.target.value);
  }

  const handleLogin = (event: FormEvent): void => {
    event.preventDefault();

    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);

    httpClient.post('/user/login', params).then((response) => {
      console.log("Response: ", response, response.request.response);
      const responseURL = response.request.responseURL;
      if (responseURL === LOGIN_SUCCESS_URL) {
        const successUserLogin: UserState = {
          isLogined: true,
          username: username
        }
        dispatch(logIn(successUserLogin));
      } else {
        const failedUserLogin: UserState = {
          isLogined: false
        }
        dispatch(logIn(failedUserLogin));
      }  

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }

  const handleSingPassLogin = (event: FormEvent): void => {
    event.preventDefault();

    httpClient.get('/singpass/login').then((response) => {
      console.log("Response: ", response);
      handleRedirectToSingPass(response.data.redirect_uri);
    }).catch((error) => {
      console.log("Error: ", error);
    })
  }

  const handleRedirectToSingPass = (redirect_uri: string) => {
    window.location.href = `http://localhost:5156/singpass/authorize?redirect_uri=${redirect_uri}`;
  }

  const isLogined = useSelector(selectIsLogined);

  if (isLogined) {
    return <Redirect to="/home" />;
  }


  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid item xs={false} sm={4} md={7} className={classes.image} />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <div className={classes.paper}>
          <Typography component="h1" variant="h5">
            Singapore Examinations and Assessment Board
          </Typography>
          <small>You are running this application with  <b>{process.env.NODE_ENV}</b> params.</small>
          <form className={classes.form}>
            <TextField 
              variant="outlined"
              fullWidth
              margin="normal"
              label="Username"
              type="text"
              value={username}
              onChange={handleUsername}
              required                
            />
            <TextField 
              variant="outlined" 
              fullWidth   
              margin="normal"            
              label="Password"
              type="password"   
              value={password}
              onChange={handlePassword}
              required             
            />
            <Button
              variant="contained"
              color="primary"
              type="submit"
              fullWidth
              onClick={handleLogin}
              
            >
              Log In
            </Button>
            <Grid container direction="row" justify="center" className={classes.singpass}>
              <Grid item>
                <Button color="primary" onClick={handleSingPassLogin}>
                  Or log in with SingPass
                </Button>
              </Grid>
            </Grid>
            <Box mt={5}>
              <Copyright />
            </Box>
          </form>
        </div>
      </Grid>
    </Grid>
  )
};

const LOGIN_SUCCESS_URL = 'http://localhost:8080/';

export default Login;