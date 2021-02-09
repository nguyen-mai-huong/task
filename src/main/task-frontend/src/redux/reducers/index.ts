import { combineReducers } from "redux";

import userReducer from '../../userModule/userReducer';

export const rootReducer = combineReducers({
  user: userReducer
})

export type RootReducerState = ReturnType<typeof rootReducer>;