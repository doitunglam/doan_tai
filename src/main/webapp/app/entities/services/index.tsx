import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Services from './services';
import ServicesDetail from './services-detail';
import ServicesUpdate from './services-update';
import ServicesDeleteDialog from './services-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServicesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Services} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ServicesDeleteDialog} />
  </>
);

export default Routes;
