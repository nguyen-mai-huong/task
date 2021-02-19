import { Paper, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, TablePagination } from "@material-ui/core";
import { ChangeEvent, useCallback, useEffect, useState } from "react";
import httpClient from '../../httpClient';

import dataTableStyles from '../../style/UserDataTableStyles';

const UserListing = () => {
  const rows = [
    { id: 1, username: "admin", email: "admin@ufinity.com" }
  ];

  const [users, setUsers] = useState(rows);
  const [userCount, setUserCount] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [page, setPage] = useState(0);
  const classes = dataTableStyles();

  const handleGetUsers = useCallback((rowsPerPage) => {
    const url = `user/list?recordsPerPage=${rowsPerPage}`;

    httpClient.get(`${url}`, { withCredentials: true }).then((response) => {
      console.log("Users: ", response.data);
      const userList = response.data.userList;
      const userCount = response.data.userCount;
      setUsers(userList);
      setUserCount(userCount);

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }, []);

  const handleGetPaginatedUsers = (page: number) => {
    const url = `user/list?recordsPerPage=${rowsPerPage}&pageNbr=${page}`;

    httpClient.get(`${url}`, { withCredentials: true }).then((response) => {
      console.log("Users: ", response.data);
      const userList = response.data.userList;
      setUsers(userList);
    }).catch((error) => {
      console.log("Error: ", error);
    })
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    handleGetPaginatedUsers(newPage);
    setPage(newPage);

  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value));
    handleGetUsers(event.target.value);
    setPage(0);
  }

  useEffect(() => {
    handleGetUsers(rowsPerPage);
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
        <TablePagination 
          rowsPerPageOptions={[5, 10, 50]}
          component="div"
          count={userCount}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </Paper>
    </div>
  )


}

export default UserListing;