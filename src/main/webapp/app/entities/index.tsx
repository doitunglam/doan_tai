import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Restaurant from './restaurant';
import Services from './services';
import ImageServices from './image-services';
import Category from './category';
import Pay from './pay';
import Invoice from './invoice';
import DetailInvoice from './detail-invoice';
import Comment from './comment';
import Hotel from './hotel';
import RoomHotel from './room-hotel';
import TypeRoom from './type-room';
import PriceRoom from './price-room';
import DetailInvoiceRoom from './detail-invoice-room';
import ImageRoom from './image-room';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}restaurant`} component={Restaurant} />
      <ErrorBoundaryRoute path={`${match.url}services`} component={Services} />
      <ErrorBoundaryRoute path={`${match.url}image-services`} component={ImageServices} />
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}pay`} component={Pay} />
      <ErrorBoundaryRoute path={`${match.url}invoice`} component={Invoice} />
      <ErrorBoundaryRoute path={`${match.url}detail-invoice`} component={DetailInvoice} />
      <ErrorBoundaryRoute path={`${match.url}comment`} component={Comment} />
      <ErrorBoundaryRoute path={`${match.url}hotel`} component={Hotel} />
      <ErrorBoundaryRoute path={`${match.url}room-hotel`} component={RoomHotel} />
      <ErrorBoundaryRoute path={`${match.url}type-room`} component={TypeRoom} />
      <ErrorBoundaryRoute path={`${match.url}price-room`} component={PriceRoom} />
      <ErrorBoundaryRoute path={`${match.url}detail-invoice-room`} component={DetailInvoiceRoom} />
      <ErrorBoundaryRoute path={`${match.url}image-room`} component={ImageRoom} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
