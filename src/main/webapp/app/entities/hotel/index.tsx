import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Hotel from './hotel';
import HotelDetail from './hotel-detail';
import HotelUpdate from './hotel-update';
import HotelDeleteDialog from './hotel-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HotelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HotelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HotelDetail} />
      <ErrorBoundaryRoute path={match.url} component={Hotel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HotelDeleteDialog} />
  </>
);

export default Routes;
