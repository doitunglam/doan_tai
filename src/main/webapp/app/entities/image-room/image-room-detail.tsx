import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './image-room.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ImageRoomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const imageRoomEntity = useAppSelector(state => state.imageRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageRoomDetailsHeading">ImageRoom</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{imageRoomEntity.id}</dd>
          <dt>
            <span id="linkImage">Link Image</span>
          </dt>
          <dd>{imageRoomEntity.linkImage}</dd>
          <dt>Room Hotel</dt>
          <dd>{imageRoomEntity.roomHotel ? imageRoomEntity.roomHotel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/image-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image-room/${imageRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageRoomDetail;
