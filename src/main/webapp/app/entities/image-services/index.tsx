import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ImageServices from './image-services';
import ImageServicesDetail from './image-services-detail';
import ImageServicesUpdate from './image-services-update';
import ImageServicesDeleteDialog from './image-services-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ImageServicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ImageServicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ImageServicesDetail} />
      <ErrorBoundaryRoute path={match.url} component={ImageServices} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ImageServicesDeleteDialog} />
  </>
);

export default Routes;
