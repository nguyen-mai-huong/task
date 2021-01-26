import httpClient from '../httpClient';
import { Redirect } from 'react-router-dom';
import useStyles from '../styles';
import { Container, Typography, Button } from '@material-ui/core';

function Home(props) {
  const classes = useStyles();

  if (!props.isLogined) {
    return <Redirect to="/" />
  }

  const handleLogout = () => {
    httpClient.get("/user/logout").then((response) => {
      console.log("Response: ", response);
      props.updateIsLogined(false);
    }).catch((error) => {
      console.log("Error: ", error);
    })

  }

  return (
    <Container>
      <Typography component="h5">
        Hello 
      </Typography>
      <Button
          variant="text"
          color="primary"
          type="submit"
          margin="normal"    
          onClick={handleLogout}
          className={classes.submit}          
      >
        Log out
      </Button>
    </Container>
  )
}

export default Home;