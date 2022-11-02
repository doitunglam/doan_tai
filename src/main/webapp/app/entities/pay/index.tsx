import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pay from './pay';
import PayDetail from './pay-detail';
import PayUpdate from './pay-update';
import PayDeleteDialog from './pay-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PayUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PayUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PayDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pay} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PayDeleteDialog} />
  </>
);

export default Routes;
