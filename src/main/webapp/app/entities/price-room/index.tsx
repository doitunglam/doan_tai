import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PriceRoom from './price-room';
import PriceRoomDetail from './price-room-detail';
import PriceRoomUpdate from './price-room-update';
import PriceRoomDeleteDialog from './price-room-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PriceRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PriceRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PriceRoomDetail} />
      <ErrorBoundaryRoute path={match.url} component={PriceRoom} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PriceRoomDeleteDialog} />
  </>
);

export default Routes;
