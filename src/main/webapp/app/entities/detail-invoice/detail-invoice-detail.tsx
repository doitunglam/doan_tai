import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './detail-invoice.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoiceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const detailInvoiceEntity = useAppSelector(state => state.detailInvoice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="detailInvoiceDetailsHeading">DetailInvoice</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{detailInvoiceEntity.id}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{detailInvoiceEntity.quantity}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{detailInvoiceEntity.price}</dd>
          <dt>Invoice</dt>
          <dd>{detailInvoiceEntity.invoice ? detailInvoiceEntity.invoice.id : ''}</dd>
          <dt>Services</dt>
          <dd>{detailInvoiceEntity.services ? detailInvoiceEntity.services.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/detail-invoice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/detail-invoice/${detailInvoiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DetailInvoiceDetail;
