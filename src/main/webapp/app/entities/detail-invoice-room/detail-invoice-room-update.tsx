import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPriceRoom } from 'app/shared/model/price-room.model';
import { getEntities as getPriceRooms } from 'app/entities/price-room/price-room.reducer';
import { IInvoice } from 'app/shared/model/invoice.model';
import { getEntities as getInvoices } from 'app/entities/invoice/invoice.reducer';
import { getEntity, updateEntity, createEntity, reset } from './detail-invoice-room.reducer';
import { IDetailInvoiceRoom } from 'app/shared/model/detail-invoice-room.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DetailInvoiceRoomUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const priceRooms = useAppSelector(state => state.priceRoom.entities);
  const invoices = useAppSelector(state => state.invoice.entities);
  const detailInvoiceRoomEntity = useAppSelector(state => state.detailInvoiceRoom.entity);
  const loading = useAppSelector(state => state.detailInvoiceRoom.loading);
  const updating = useAppSelector(state => state.detailInvoiceRoom.updating);
  const updateSuccess = useAppSelector(state => state.detailInvoiceRoom.updateSuccess);
  const handleClose = () => {
    props.history.push('/detail-invoice-room');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPriceRooms({}));
    dispatch(getInvoices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...detailInvoiceRoomEntity,
      ...values,
      priceRoom: priceRooms.find(it => it.id.toString() === values.priceRoom.toString()),
      invoice: invoices.find(it => it.id.toString() === values.invoice.toString()),
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
          ...detailInvoiceRoomEntity,
          priceRoom: detailInvoiceRoomEntity?.priceRoom?.id,
          invoice: detailInvoiceRoomEntity?.invoice?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.detailInvoiceRoom.home.createOrEditLabel" data-cy="DetailInvoiceRoomCreateUpdateHeading">
            Create or edit a DetailInvoiceRoom
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
                <ValidatedField name="id" required readOnly id="detail-invoice-room-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Price" id="detail-invoice-room-price" name="price" data-cy="price" type="text" />
              <ValidatedField id="detail-invoice-room-priceRoom" name="priceRoom" data-cy="priceRoom" label="Price Room" type="select">
                <option value="" key="0" />
                {priceRooms
                  ? priceRooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="detail-invoice-room-invoice" name="invoice" data-cy="invoice" label="Invoice" type="select">
                <option value="" key="0" />
                {invoices
                  ? invoices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/detail-invoice-room" replace color="info">
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

export default DetailInvoiceRoomUpdate;
