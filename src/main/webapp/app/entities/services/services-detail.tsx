import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './services.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ServicesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const servicesEntity = useAppSelector(state => state.services.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="servicesDetailsHeading">Services</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{servicesEntity.id}</dd>
          <dt>
            <span id="serviceName">Service Name</span>
          </dt>
          <dd>{servicesEntity.serviceName}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{servicesEntity.price}</dd>
          <dt>
            <span id="unit">Unit</span>
          </dt>
          <dd>{servicesEntity.unit}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{servicesEntity.quantity}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{servicesEntity.address}</dd>
          <dt>Restaurant</dt>
          <dd>{servicesEntity.restaurant ? servicesEntity.restaurant.id : ''}</dd>
          <dt>Category</dt>
          <dd>{servicesEntity.category ? servicesEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/services" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/services/${servicesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServicesDetail;
