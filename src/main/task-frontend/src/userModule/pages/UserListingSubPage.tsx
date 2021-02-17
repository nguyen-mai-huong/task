import { Paper, TableContainer, Table, TableHead, TableRow, TableCell, TableBody } from "@material-ui/core";
import { useCallback, useEffect, useState } from "react";
import httpClient from '../../httpClient';

import dataTableStyles from '../../style/UserDataTableStyles';

const UserListing = () => {
  const rows = [
    { id: 1, username: "admin", email: "admin@ufinity.com" },
    { id: 5, username: "tester01", email: "tester01@ufinity.com"},
    { id: 10, username: "tester02", email: "tester01@ufinity.com"},
  ];

  const [users, setUsers] = useState(rows);
  const classes = dataTableStyles();

  const handleGetUsers = useCallback(() => {
    const params = {
      recordsPerPage: 5
    };
    httpClient.get("user/list", { params }).then((response) => {
      console.log("Users: ", response.data);
      const userList = response.data.data;
      setUsers(userList);

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }, []);

  useEffect(() => {
    handleGetUsers();
  }, [handleGetUsers]);

  return (
    <div className={classes.root}>
      <Paper className={classes.paper}>
        <TableContainer>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                <TableCell variant="head">
                  ID
                </TableCell>
                <TableCell variant="head">
                  Username
                </TableCell>
                <TableCell variant="head">
                  Email
                </TableCell>

              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user, index) => {
                return (
                  <TableRow key={user.id}>
                    <TableCell>
                      {user.id}
                    </TableCell>
                    <TableCell>
                      {user.username}
                    </TableCell>
                    <TableCell>
                      {user.email}
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </div>
  )


}

export default UserListing;