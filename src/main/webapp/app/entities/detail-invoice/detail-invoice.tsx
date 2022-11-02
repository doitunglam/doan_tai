import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './detail-invoice.reducer';
import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoice = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const detailInvoiceList = useAppSelector(state => state.detailInvoice.entities);
  const loading = useAppSelector(state => state.detailInvoice.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="detail-invoice-heading" data-cy="DetailInvoiceHeading">
        Detail Invoices
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Detail Invoice
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {detailInvoiceList && detailInvoiceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Invoice</th>
                <th>Services</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {detailInvoiceList.map((detailInvoice, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${detailInvoice.id}`} color="link" size="sm">
                      {detailInvoice.id}
                    </Button>
                  </td>
                  <td>{detailInvoice.quantity}</td>
                  <td>{detailInvoice.price}</td>
                  <td>{detailInvoice.invoice ? <Link to={`invoice/${detailInvoice.invoice.id}`}>{detailInvoice.invoice.id}</Link> : ''}</td>
                  <td>
                    {detailInvoice.services ? <Link to={`services/${detailInvoice.services.id}`}>{detailInvoice.services.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${detailInvoice.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${detailInvoice.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${detailInvoice.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Detail Invoices found</div>
        )}
      </div>
    </div>
  );
};

export default DetailInvoice;
