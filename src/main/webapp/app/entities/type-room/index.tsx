import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TypeRoom from './type-room';
import TypeRoomDetail from './type-room-detail';
import TypeRoomUpdate from './type-room-update';
import TypeRoomDeleteDialog from './type-room-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TypeRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TypeRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TypeRoomDetail} />
      <ErrorBoundaryRoute path={match.url} component={TypeRoom} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TypeRoomDeleteDialog} />
  </>
);

export default Routes;
