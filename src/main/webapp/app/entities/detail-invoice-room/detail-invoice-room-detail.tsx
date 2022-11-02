import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './detail-invoice-room.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoiceRoomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const detailInvoiceRoomEntity = useAppSelector(state => state.detailInvoiceRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="detailInvoiceRoomDetailsHeading">DetailInvoiceRoom</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{detailInvoiceRoomEntity.id}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{detailInvoiceRoomEntity.price}</dd>
          <dt>Price Room</dt>
          <dd>{detailInvoiceRoomEntity.priceRoom ? detailInvoiceRoomEntity.priceRoom.id : ''}</dd>
          <dt>Invoice</dt>
          <dd>{detailInvoiceRoomEntity.invoice ? detailInvoiceRoomEntity.invoice.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/detail-invoice-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/detail-invoice-room/${detailInvoiceRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DetailInvoiceRoomDetail;
