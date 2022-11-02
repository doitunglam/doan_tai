import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DetailInvoiceRoom from './detail-invoice-room';
import DetailInvoiceRoomDetail from './detail-invoice-room-detail';
import DetailInvoiceRoomUpdate from './detail-invoice-room-update';
import DetailInvoiceRoomDeleteDialog from './detail-invoice-room-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DetailInvoiceRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DetailInvoiceRoomUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DetailInvoiceRoomDetail} />
      <ErrorBoundaryRoute path={match.url} component={DetailInvoiceRoom} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DetailInvoiceRoomDeleteDialog} />
  </>
);

export default Routes;
