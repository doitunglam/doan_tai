import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './detail-invoice-room.reducer';
import { IDetailInvoiceRoom } from 'app/shared/model/detail-invoice-room.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoiceRoom = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const detailInvoiceRoomList = useAppSelector(state => state.detailInvoiceRoom.entities);
  const loading = useAppSelector(state => state.detailInvoiceRoom.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="detail-invoice-room-heading" data-cy="DetailInvoiceRoomHeading">
        Detail Invoice Rooms
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Detail Invoice Room
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {detailInvoiceRoomList && detailInvoiceRoomList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Price</th>
                <th>Price Room</th>
                <th>Invoice</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {detailInvoiceRoomList.map((detailInvoiceRoom, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${detailInvoiceRoom.id}`} color="link" size="sm">
                      {detailInvoiceRoom.id}
                    </Button>
                  </td>
                  <td>{detailInvoiceRoom.price}</td>
                  <td>
                    {detailInvoiceRoom.priceRoom ? (
                      <Link to={`price-room/${detailInvoiceRoom.priceRoom.id}`}>{detailInvoiceRoom.priceRoom.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {detailInvoiceRoom.invoice ? (
                      <Link to={`invoice/${detailInvoiceRoom.invoice.id}`}>{detailInvoiceRoom.invoice.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${detailInvoiceRoom.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${detailInvoiceRoom.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${detailInvoiceRoom.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Detail Invoice Rooms found</div>
        )}
      </div>
    </div>
  );
};

export default DetailInvoiceRoom;
