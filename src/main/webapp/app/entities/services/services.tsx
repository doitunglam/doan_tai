import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './services.reducer';
import { IServices } from 'app/shared/model/services.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Services = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const servicesList = useAppSelector(state => state.services.entities);
  const loading = useAppSelector(state => state.services.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="services-heading" data-cy="ServicesHeading">
        Services
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Services
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {servicesList && servicesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Service Name</th>
                <th>Price</th>
                <th>Unit</th>
                <th>Quantity</th>
                <th>Address</th>
                <th>Restaurant</th>
                <th>Category</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {servicesList.map((services, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${services.id}`} color="link" size="sm">
                      {services.id}
                    </Button>
                  </td>
                  <td>{services.serviceName}</td>
                  <td>{services.price}</td>
                  <td>{services.unit}</td>
                  <td>{services.quantity}</td>
                  <td>{services.address}</td>
                  <td>{services.restaurant ? <Link to={`restaurant/${services.restaurant.id}`}>{services.restaurant.id}</Link> : ''}</td>
                  <td>{services.category ? <Link to={`category/${services.category.id}`}>{services.category.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${services.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${services.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${services.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Services found</div>
        )}
      </div>
    </div>
  );
};

export default Services;
