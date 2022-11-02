import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './room-hotel.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RoomHotelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const roomHotelEntity = useAppSelector(state => state.roomHotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomHotelDetailsHeading">RoomHotel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{roomHotelEntity.id}</dd>
          <dt>
            <span id="roomName">Room Name</span>
          </dt>
          <dd>{roomHotelEntity.roomName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{roomHotelEntity.description}</dd>
          <dt>
            <span id="roomStatus">Room Status</span>
          </dt>
          <dd>{roomHotelEntity.roomStatus}</dd>
          <dt>Type Room</dt>
          <dd>{roomHotelEntity.typeRoom ? roomHotelEntity.typeRoom.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/room-hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room-hotel/${roomHotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomHotelDetail;
