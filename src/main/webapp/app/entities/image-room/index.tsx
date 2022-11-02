import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ImageRoom from './image-room';
import ImageRoomDetail from './image-room-detail';
import ImageRoomUpdate from './image-room-update';
import ImageRoomDeleteDialog from './image-room-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ImageRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ImageRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ImageRoomDetail} />
      <ErrorBoundaryRoute path={match.url} component={ImageRoom} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ImageRoomDeleteDialog} />
  </>
);

export default Routes;
