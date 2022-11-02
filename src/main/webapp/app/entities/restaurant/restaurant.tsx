import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './restaurant.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Restaurant = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const restaurantList = useAppSelector(state => state.restaurant.entities);
  const loading = useAppSelector(state => state.restaurant.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="restaurant-heading" data-cy="RestaurantHeading">
        Restaurants
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Restaurant
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {restaurantList && restaurantList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Res Name</th>
                <th>Address</th>
                <th>Phone</th>
                <th>Token</th>
                <th>Linkweb</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {restaurantList.map((restaurant, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${restaurant.id}`} color="link" size="sm">
                      {restaurant.id}
                    </Button>
                  </td>
                  <td>{restaurant.resName}</td>
                  <td>{restaurant.address}</td>
                  <td>{restaurant.phone}</td>
                  <td>{restaurant.token}</td>
                  <td>{restaurant.linkweb}</td>
                  <td>
                    {restaurant.users
                      ? restaurant.users.map((val, j) => (
                          <span key={j}>
                            {val.login}
                            {j === restaurant.users.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${restaurant.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${restaurant.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${restaurant.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Restaurants found</div>
        )}
      </div>
    </div>
  );
};

export default Restaurant;
