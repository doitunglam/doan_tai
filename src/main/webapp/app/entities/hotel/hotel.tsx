import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './hotel.reducer';
import { IHotel } from 'app/shared/model/hotel.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Hotel = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const hotelList = useAppSelector(state => state.hotel.entities);
  const loading = useAppSelector(state => state.hotel.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="hotel-heading" data-cy="HotelHeading">
        Hotels
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Hotel
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hotelList && hotelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Hotel Name</th>
                <th>Address</th>
                <th>Phone</th>
                <th>Token</th>
                <th>Active</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hotelList.map((hotel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${hotel.id}`} color="link" size="sm">
                      {hotel.id}
                    </Button>
                  </td>
                  <td>{hotel.hotelName}</td>
                  <td>{hotel.address}</td>
                  <td>{hotel.phone}</td>
                  <td>{hotel.token}</td>
                  <td>{hotel.active}</td>
                  <td>
                    {hotel.users
                      ? hotel.users.map((val, j) => (
                          <span key={j}>
                            {val.login}
                            {j === hotel.users.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${hotel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${hotel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${hotel.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Hotels found</div>
        )}
      </div>
    </div>
  );
};

export default Hotel;
