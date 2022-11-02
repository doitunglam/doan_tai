import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInvoice } from 'app/shared/model/invoice.model';
import { getEntities as getInvoices } from 'app/entities/invoice/invoice.reducer';
import { IServices } from 'app/shared/model/services.model';
import { getEntities as getServices } from 'app/entities/services/services.reducer';
import { getEntity, updateEntity, createEntity, reset } from './detail-invoice.reducer';
import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoiceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const invoices = useAppSelector(state => state.invoice.entities);
  const services = useAppSelector(state => state.services.entities);
  const detailInvoiceEntity = useAppSelector(state => state.detailInvoice.entity);
  const loading = useAppSelector(state => state.detailInvoice.loading);
  const updating = useAppSelector(state => state.detailInvoice.updating);
  const updateSuccess = useAppSelector(state => state.detailInvoice.updateSuccess);
  const handleClose = () => {
    props.history.push('/detail-invoice');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getInvoices({}));
    dispatch(getServices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...detailInvoiceEntity,
      ...values,
      invoice: invoices.find(it => it.id.toString() === values.invoice.toString()),
      services: services.find(it => it.id.toString() === values.services.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...detailInvoiceEntity,
          invoice: detailInvoiceEntity?.invoice?.id,
          services: detailInvoiceEntity?.services?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.detailInvoice.home.createOrEditLabel" data-cy="DetailInvoiceCreateUpdateHeading">
            Create or edit a DetailInvoice
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="detail-invoice-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Quantity" id="detail-invoice-quantity" name="quantity" data-cy="quantity" type="text" />
              <ValidatedField label="Price" id="detail-invoice-price" name="price" data-cy="price" type="text" />
              <ValidatedField id="detail-invoice-invoice" name="invoice" data-cy="invoice" label="Invoice" type="select">
                <option value="" key="0" />
                {invoices
                  ? invoices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="detail-invoice-services" name="services" data-cy="services" label="Services" type="select">
                <option value="" key="0" />
                {services
                  ? services.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/detail-invoice" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DetailInvoiceUpdate;
