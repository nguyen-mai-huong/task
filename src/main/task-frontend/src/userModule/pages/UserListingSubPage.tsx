import { Paper, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, TablePagination } from "@material-ui/core";
import { ChangeEvent, useCallback, useEffect, useState } from "react";
import httpClient from '../../httpClient';

import dataTableStyles from '../../style/UserDataTableStyles';

const UserListing = () => {
  const rows = [
    { id: 1, username: "admin", email: "admin@ufinity.com" },
    { id: 5, username: "tester01", email: "tester01@ufinity.com"},
    { id: 10, username: "tester02", email: "tester01@ufinity.com"},
  ];

  const [users, setUsers] = useState(rows);
  const [userCount, setUserCount] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [page, setPage] = useState(0);
  const classes = dataTableStyles();

  const handleGetUsers = useCallback(() => {
    const url = `user/list?recordsPerPage=${rowsPerPage}`;

    const headers = {
      Cookie: document.cookie
    };

    httpClient.get(`${url}`, { headers: headers }).then((response) => {
      console.log("Users: ", response.data);
      const userList = response.data.userList;
      const userCount = response.data.userCount;
      setUsers(userList);
      setUserCount(userCount);

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }, []);

  const handleGetPaginatedUsers = (next: boolean) => {
    let url;
    if (next) {
      const rightCursor = Math.max(...users.map(user => user.id));
      url = `user/list?recordsPerPage=${rowsPerPage}&rightCursor=${rightCursor}`;
    } else {
      const leftCursor = Math.min(...users.map(user => user.id));
      url = `user/list?recordsPerPage=${rowsPerPage}&leftCursor=${leftCursor}`;
    }

    const headers = {
      Cookie: document.cookie
    };

    httpClient.get(`${url}`, { headers: headers }).then((response) => {
      console.log("Users: ", response.data);
      const userList = response.data.userList;

      if (!next) {
        userList.reverse();
      }

      setUsers(userList);
    }).catch((error) => {
      console.log("Error: ", error);
    })
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    const next = page < newPage ? true : false;
    handleGetPaginatedUsers(next);
    setPage(newPage);

  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  }

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
              {users.length > 0 && users.map((user, index) => {
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