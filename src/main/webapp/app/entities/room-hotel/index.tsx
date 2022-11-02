import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RoomHotel from './room-hotel';
import RoomHotelDetail from './room-hotel-detail';
import RoomHotelUpdate from './room-hotel-update';
import RoomHotelDeleteDialog from './room-hotel-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RoomHotelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RoomHotelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RoomHotelDetail} />
      <ErrorBoundaryRoute path={match.url} component={RoomHotel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RoomHotelDeleteDialog} />
  </>
);

export default Routes;
