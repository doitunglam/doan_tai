import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './restaurant.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RestaurantDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const restaurantEntity = useAppSelector(state => state.restaurant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="restaurantDetailsHeading">Restaurant</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{restaurantEntity.id}</dd>
          <dt>
            <span id="resName">Res Name</span>
          </dt>
          <dd>{restaurantEntity.resName}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{restaurantEntity.address}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{restaurantEntity.phone}</dd>
          <dt>
            <span id="token">Token</span>
          </dt>
          <dd>{restaurantEntity.token}</dd>
          <dt>
            <span id="linkweb">Linkweb</span>
          </dt>
          <dd>{restaurantEntity.linkweb}</dd>
          <dt>User</dt>
          <dd>
            {restaurantEntity.users
              ? restaurantEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.login}</a>
                    {restaurantEntity.users && i === restaurantEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/restaurant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/restaurant/${restaurantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RestaurantDetail;
