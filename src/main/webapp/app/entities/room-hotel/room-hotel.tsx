import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './room-hotel.reducer';
import { IRoomHotel } from 'app/shared/model/room-hotel.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RoomHotel = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const roomHotelList = useAppSelector(state => state.roomHotel.entities);
  const loading = useAppSelector(state => state.roomHotel.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="room-hotel-heading" data-cy="RoomHotelHeading">
        Room Hotels
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Room Hotel
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {roomHotelList && roomHotelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Room Name</th>
                <th>Description</th>
                <th>Room Status</th>
                <th>Type Room</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {roomHotelList.map((roomHotel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${roomHotel.id}`} color="link" size="sm">
                      {roomHotel.id}
                    </Button>
                  </td>
                  <td>{roomHotel.roomName}</td>
                  <td>{roomHotel.description}</td>
                  <td>{roomHotel.roomStatus}</td>
                  <td>{roomHotel.typeRoom ? <Link to={`type-room/${roomHotel.typeRoom.id}`}>{roomHotel.typeRoom.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${roomHotel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${roomHotel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${roomHotel.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Room Hotels found</div>
        )}
      </div>
    </div>
  );
};

export default RoomHotel;
