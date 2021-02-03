import { useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import httpClient from "../httpClient";

function SingPassCallback(props) {
  console.log("Current location is: ", window.location);
  const [username, setUsername] = useState('');

  useEffect(() => {
    const abortController = new AbortController();
    sendAuthCodeToAppServer();
    return () => abortController.abort();

  })

  const sendAuthCodeToAppServer = () => {
    const callbackParams = new URLSearchParams(window.location.search);
    if (callbackParams.get('code') === undefined || callbackParams.get('code') === null){
      return false;
    }

    const params = new URLSearchParams();
    params.append("code", callbackParams.get('code'));
    
    httpClient.post('login/oauth2/code/singpass', params).then((response) => {
      console.log("Response after sending auth code is: ", response);
      if (response.data.code === "ok") {
        props.updateIsLogined(true);
        setUsername(response.data.username);
      }
    }).catch((error) => {
      console.log("Error is: ", error);  
    })
    
  } 

  if (props.isLogined) {
    console.log("Can I see username here? ", {username});
    return (
      <Redirect 
        to = {{
          pathname: '/home',
          state: {
            username: `${username}`
          }
        }}
      />
    )
  } 

  return (
    <div>
      Sending auth code to app server 
    </div>
  )

  
}

export default SingPassCallback;