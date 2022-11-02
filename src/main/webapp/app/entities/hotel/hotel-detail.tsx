import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './hotel.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const HotelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const hotelEntity = useAppSelector(state => state.hotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hotelDetailsHeading">Hotel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{hotelEntity.id}</dd>
          <dt>
            <span id="hotelName">Hotel Name</span>
          </dt>
          <dd>{hotelEntity.hotelName}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{hotelEntity.address}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{hotelEntity.phone}</dd>
          <dt>
            <span id="token">Token</span>
          </dt>
          <dd>{hotelEntity.token}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{hotelEntity.active}</dd>
          <dt>User</dt>
          <dd>
            {hotelEntity.users
              ? hotelEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.login}</a>
                    {hotelEntity.users && i === hotelEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hotel/${hotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HotelDetail;
