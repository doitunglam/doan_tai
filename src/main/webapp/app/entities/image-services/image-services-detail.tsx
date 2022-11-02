import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './image-services.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ImageServicesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const imageServicesEntity = useAppSelector(state => state.imageServices.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageServicesDetailsHeading">ImageServices</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{imageServicesEntity.id}</dd>
          <dt>
            <span id="linkimage">Linkimage</span>
          </dt>
          <dd>{imageServicesEntity.linkimage}</dd>
          <dt>Services</dt>
          <dd>{imageServicesEntity.services ? imageServicesEntity.services.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/image-services" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image-services/${imageServicesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageServicesDetail;
