import { RootReducerState } from '../redux/reducers/index';

export const selectIsLogined = (state: RootReducerState) => state.user.isLogined;
export const selectUsername = (state: RootReducerState) => state.user.username;