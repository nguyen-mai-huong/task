import { useEffect } from "react";
import { Redirect } from "react-router-dom";

function Waiting(props) {
  useEffect(() => {
    props.updateIsLogined(true);
  }) 

  if (props.isLogined) {
    return <Redirect to = "/home" />
  }

  return (

    <div>
      Wating page after Singpass login
    </div>
  )
}

export default Waiting;