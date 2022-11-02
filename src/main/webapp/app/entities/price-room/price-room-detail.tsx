import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './price-room.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PriceRoomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const priceRoomEntity = useAppSelector(state => state.priceRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="priceRoomDetailsHeading">PriceRoom</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{priceRoomEntity.id}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{priceRoomEntity.price}</dd>
          <dt>
            <span id="unit">Unit</span>
          </dt>
          <dd>{priceRoomEntity.unit}</dd>
          <dt>Room Hotel</dt>
          <dd>{priceRoomEntity.roomHotel ? priceRoomEntity.roomHotel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/price-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/price-room/${priceRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PriceRoomDetail;
