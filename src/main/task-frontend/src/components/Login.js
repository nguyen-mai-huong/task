import { useState } from 'react';
import { Redirect } from 'react-router-dom';

import httpClient from '../httpClient';

import { Grid, Typography, TextField, Button, Box, CssBaseline, Paper, Link } from '@material-ui/core';
import useStyles from '../styles';
import Copyright from '../components/Copyright';

function Login(props) {
  // process.env.PORT = 3000;
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const classes = useStyles();

  const handleUsername = (event) => {
    setUsername(event.target.value);
  }

  const handlePassword = (event) => {
    setPassword(event.target.value);
  }

  const handleLogin = (event) => {
    event.preventDefault();

    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);

    httpClient.post('/user/login', params).then((response) => {
      console.log("Response: ", response, response.request.response);
      const responseURL = response.request.responseURL;
      if (responseURL === LOGIN_SUCCESS_URL) {
        props.updateIsLogined(true);
      } else {
        props.updateIsLogined(false);

      }  

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }

  if (props.isLogined) {
    return <Redirect to="/home" />;
  };



  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid order={1} item xs={false} sm={4} md={7} className={classes.image} />
      <Grid order={2} item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
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
              margin="normal"    
              onClick={handleLogin}
            >
              Log In
            </Button>
            <Grid container direction="row" justify="center" className={classes.singpass}>
              <Grid item>
                <Link href="#" variant="body2" color="textSecondary">
                  Or log in with SingPass
                </Link>
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


}


const LOGIN_SUCCESS_URL = 'http://localhost:8080/';

export default Login;
