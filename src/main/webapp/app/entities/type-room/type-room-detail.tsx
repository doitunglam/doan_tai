import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './type-room.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TypeRoomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const typeRoomEntity = useAppSelector(state => state.typeRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="typeRoomDetailsHeading">TypeRoom</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{typeRoomEntity.id}</dd>
          <dt>
            <span id="typeName">Type Name</span>
          </dt>
          <dd>{typeRoomEntity.typeName}</dd>
          <dt>Hotel</dt>
          <dd>{typeRoomEntity.hotel ? typeRoomEntity.hotel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/type-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/type-room/${typeRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TypeRoomDetail;
