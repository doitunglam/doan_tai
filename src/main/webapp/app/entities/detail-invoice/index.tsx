import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DetailInvoice from './detail-invoice';
import DetailInvoiceDetail from './detail-invoice-detail';
import DetailInvoiceUpdate from './detail-invoice-update';
import DetailInvoiceDeleteDialog from './detail-invoice-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DetailInvoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DetailInvoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DetailInvoiceDetail} />
      <ErrorBoundaryRoute path={match.url} component={DetailInvoice} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DetailInvoiceDeleteDialog} />
  </>
);

export default Routes;
