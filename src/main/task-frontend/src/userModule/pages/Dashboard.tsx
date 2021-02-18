import { useDispatch, useSelector } from "react-redux";
import { Redirect, useRouteMatch, Link, Router, Switch, Route } from "react-router-dom";
import React, { useState } from 'react';

import { selectIsLogined, selectUsername } from "../userSelector";
import httpClient from '../../httpClient';
import { logOut } from "../userActions";
import { UserState } from "../UserState";
import { Home } from "../pages/HomeSubPage";

import { createBrowserHistory } from 'history';

import { 
  Container, 
  Typography, 
  Button, 
  CssBaseline, 
  AppBar, 
  Toolbar, 
  IconButton, 
  Drawer, 
  Divider, 
  List, 
  ListItem, 
  ListItemIcon, 
  ListItemText, 
  Paper, 
  TableContainer, 
  Table, 
  TableHead,
  TableRow,
  TableCell, 
  TableBody
} from '@material-ui/core';

// import useStyles from '../../style/styles';
import useStyles from '../../style/HomeStyles';
import dataTableStyles from '../../style/UserDataTableStyles';
import clsx from 'clsx';
import HomeIcon from '@material-ui/icons/Home';
import PeopleIcon from '@material-ui/icons/People';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import UserListing from "./UserListingSubPage";

const Dashboard = () => {
  const isLogined = useSelector(selectIsLogined);
  const username = useSelector(selectUsername);
  const [open, setOpen] = useState(true);
  const [users, setUsers] = useState([]);

  const history = createBrowserHistory();


  // const userInfo: UserInfo = {
  //   username: useSelector(selectUsername)
  // };
  

  const { url, path } = useRouteMatch();

  const dispatch = useDispatch();
  
  const classes = useStyles();
  const tableClasses = dataTableStyles();

  if (!isLogined) {
    return <Redirect to="/" />;
  }

  const handleLogout = (): void => {
    httpClient.get("/user/logout").then((response) => {
      console.log("Response: ", response);

      document.cookie = '';
      const successUserLogout: UserState = {
        isLogined: false
      }
      dispatch(logOut(successUserLogout));
    }).catch((error) => {
      console.log("Error: ", error);
    })

  }

  const handleGetUsers = (): void => {
    const params = {
      recordsPerPage: 5
    };
    httpClient.get("user/list", { params }).then((response) => {
      console.log("Response: ", response);
      const users = response.data;
      setUsers(users);
    }).catch((error) => {
      console.log("Error: ", error);
    })
  }


  const handleDrawerOpen = () => {
    setOpen(true);
  };
  const handleDrawerClose = () => {
    setOpen(false);
  };
  // User data table

  const rows = [
    { id: 1, username: "admin", email: "admin@ufinity.com" },
    { id: 5, username: "tester01", email: "tester01@ufinity.com"},
    { id: 10, username: "tester02", email: "tester01@ufinity.com"},
  ];



  return (
    <Router history={history}>
    <div className={classes.root}>
        <CssBaseline />
        <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
          <Toolbar className={classes.toolbar}>
            <IconButton
              edge="start"
              color="inherit"
              aria-label="open drawer"
              onClick={handleDrawerOpen}
              className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
            >
              <MenuIcon />
            </IconButton>
            <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
              
            </Typography>
            <Button color="inherit" onClick={handleLogout}>
              Log out
            </Button>
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          classes={{
            paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
          }}
          open={open}
        >
          <div className={classes.toolbarIcon}>
            <IconButton onClick={handleDrawerClose}>
              <ChevronLeftIcon />
            </IconButton>
          </div>
          <Divider />

            <div>
              <nav>
                <ul>
                  <li>
                    <Link to={`${url}/home`}>Home</Link>
                  </li>
                  <li>
                    <Link to={`${url}/user-listing`}>User Listing</Link>
                  </li>
                </ul>
              </nav>
            </div>

        </Drawer>
        <main className={classes.content}>
          <div className={classes.appBarSpacer} />
          <Container maxWidth="lg" className={classes.container}>
          <Switch>
              <Route path={`${path}/home`} component={UserListing} />
              <Route path={`${path}/user-listing`} component={UserListing} />
            </Switch>

            

          </Container>
        </main>
      </div>
      </Router>
  );
}

export default Dashboard;